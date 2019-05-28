package com.pflager.redbook.kvs;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser;

import jna.extra.GDI32Extra;
import jna.extra.User32Extra;
import jna.extra.WinGDIExtra;

public class ImageCompareJNA extends JFrame {
	public String javaHome;
	public String javaBin;
	public String classpath;
	private Process process;
	private File FirstFile;
	private File SecondFile;
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	private String ImageName;
	private String fileformat = "jpg";
	private HWND hWnd;

	private double MaxWaitTime = 10000; // Milliseconds
	Process PS = null;
	private static String AppWindowName;

	public ImageCompareJNA() throws HeadlessException {
		javaHome = System.getProperty("java.home");
		javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		classpath = System.getProperty("java.class.path");
		FirstFile = null;
		SecondFile = null;
	}

	protected void finalize() throws Throwable {
		process.destroy();
		if (FirstFile != null)
			FirstFile.delete();
		if (SecondFile != null)
			FirstFile.delete();
	}

	public boolean capture(String ExportPath) {

		if (hWnd == null) {
			System.out.print("Window not found!");
			return false;
		}

		HDC hdcWindow = User32.INSTANCE.GetDC(hWnd);
		HDC hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);

		RECT bounds = new RECT();
		User32Extra.INSTANCE.GetClientRect(hWnd, bounds);

		int width = bounds.right - bounds.left;
		int height = bounds.bottom - bounds.top;

		HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, width, height);

		HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
		GDI32Extra.INSTANCE.BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow, 0, 0, WinGDIExtra.SRCCOPY);

		GDI32.INSTANCE.SelectObject(hdcMemDC, hOld);
		GDI32.INSTANCE.DeleteDC(hdcMemDC);

		BITMAPINFO bmi = new BITMAPINFO();
		bmi.bmiHeader.biWidth = width;
		bmi.bmiHeader.biHeight = -height;
		bmi.bmiHeader.biPlanes = 1;
		bmi.bmiHeader.biBitCount = 32;
		bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

		Memory buffer = new Memory(width * height * 4);
		GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, height, buffer, bmi, WinGDI.DIB_RGB_COLORS);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, buffer.getIntArray(0, width * height), 0, width);

		File outputfile = new File(ExportPath + ImageName);

		try {
			ImageIO.write(image, fileformat, outputfile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		GDI32.INSTANCE.DeleteObject(hBitmap);
		User32.INSTANCE.ReleaseDC(hWnd, hdcWindow);
		return true;
	}

	public static boolean IdenticalImage(File FirstFile, File SecondFile) {
		try {
			BufferedImage biA = ImageIO.read(FirstFile);
			DataBuffer dbA = biA.getData().getDataBuffer();
			int sizeA = dbA.getSize();
			BufferedImage biB = ImageIO.read(SecondFile);
			DataBuffer dbB = biB.getData().getDataBuffer();
			int sizeB = dbB.getSize();
			if (sizeA == sizeB) {
				for (int i = 0; i < sizeA; i++) {
					if (dbA.getElem(i) != dbB.getElem(i)) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println("Failed to compare image files.");
			return false;
		}
	}

	private boolean ExecuteEXE(String CFileName) {

		try {
			PS = new ProcessBuilder("redbook-1.1-src/src/" + CFileName + ".exe").start();
			AppWindowName = "redbook-1.1-src\\src\\" + CFileName + ".exe";
			hWnd = null;
			hWnd = User32.INSTANCE.FindWindow(null, CFileName);
			if (hWnd == null) {
				hWnd = User32.INSTANCE.FindWindow(null, AppWindowName);
				if (hWnd == null) {
					hWnd = User32.INSTANCE.FindWindow(null, CFileName + ".c");
					System.out.println("AppWindowName" + AppWindowName);
				}
			}
			double waitTime = 0;
			while ((hWnd == null) && (waitTime < MaxWaitTime)) {
				waitTime = waitTime + 100;
				try {
					Thread.sleep(100);
					System.out.println("Wait time" + waitTime);
					hWnd = User32.INSTANCE.FindWindow(null, CFileName);
					if (hWnd == null) {
						hWnd = User32.INSTANCE.FindWindow(null, AppWindowName);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
//		InputStream is = PS.getInputStream();
//		InputStreamReader isr = new InputStreamReader(is);
//		BufferedReader br = new BufferedReader(isr);
//		String line;
		return true;

	}

	public boolean CaptureCImage(String WindowName) throws InterruptedException {
		if (ExecuteEXE(WindowName)) {// Running c application with window name
			Thread.sleep(8000);
			hWnd = User32.INSTANCE.FindWindow(null, WindowName);
			if (hWnd == null) {
				hWnd = User32.INSTANCE.FindWindow(null, AppWindowName);
				if (hWnd == null) {
					hWnd = User32.INSTANCE.FindWindow(null, WindowName + ".c");
				}
			}
			if (hWnd != null) {
				ImageName = WindowName + "CImage";
				if (capture("redbook-1.1-src/src/")) { // Capturing c application window
					PS.destroy(); // Ending C executable process
					return true;
				}
			} else {
				PS.destroy(); // Ending C executable process
				return false;
			}
		}
		return false;
	}

	/**
	 *
	 * @param WindowName Name Name of the active Jglut window, this also used for
	 *                   running the C program
	 * @return Returns true if the image are identical
	 */
	public boolean CompareImage(String WindowName) {
		ImageName = WindowName;
		hWnd = User32.INSTANCE.FindWindow(null, WindowName);// finding jglut window

		User32.INSTANCE.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
		if (capture("redbook-1.1-src/src/")) { // Capturing jglut window
			if (ExecuteEXE(WindowName)) {// Running c application with window name
				hWnd = User32.INSTANCE.FindWindow(null, AppWindowName);
				ImageName = WindowName + "CImage";
				if (capture("redbook-1.1-src/src/")) { // Capturing c application window
					PS.destroy(); // Ending C executable process
					FirstFile = new File("redbook-1.1-src/src/" + WindowName);
					SecondFile = new File("redbook-1.1-src/src/" + WindowName + "CImage");
					if (IdenticalImage(FirstFile, SecondFile)) {
						FirstFile.delete();
						SecondFile.delete();
						System.out.println("Identical Image");
						return true;
					} else {
						FirstFile.delete();
						SecondFile.delete();
						System.out.println("Different Image");
						return false;
					}
				}
			}
		}
		return false;
	}

	public boolean CompareImageSec(String WindowName) throws InterruptedException {
		System.out.println("CompareImageSec");
		ImageName = WindowName;
		hWnd = User32.INSTANCE.FindWindow(null, WindowName);// finding jglut window
		if (hWnd == null) {
			hWnd = User32.INSTANCE.FindWindow(null, WindowName + ".java");
		}
		double waitTime = 0;
		while ((hWnd == null) && (waitTime < MaxWaitTime)) {
			waitTime = waitTime + 1000;
			try {
				Thread.sleep(1000);
				hWnd = User32.INSTANCE.FindWindow(null, WindowName);// finding jglut window
				if (hWnd == null) {
					hWnd = User32.INSTANCE.FindWindow(null, WindowName + ".java");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Thread.sleep(8000);
		// User32.INSTANCE.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
		if (capture("redbook-1.1-src/src/")) { // Capturing jglut window
			File FirstFile = new File("redbook-1.1-src/src/" + WindowName);
			File SecondFile = new File("redbook-1.1-src/src/" + WindowName + "CImage");
			if (IdenticalImage(FirstFile, SecondFile)) {
				FirstFile.delete();
				SecondFile.delete();
				System.out.println("Identical Image");
				// User32.INSTANCE.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
				return true;
			} else {
				FirstFile.delete();
				SecondFile.delete();
				System.out.println("Different Image");
				// User32.INSTANCE.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
				return false;
			}
		}
		// User32.INSTANCE.PostMessage(hWnd, WinUser.WM_CLOSE, null, null);
		return false;
	}

	public void RunNewProcess(String classname) throws IOException {
		ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, classname);
		process = builder.inheritIO().start();
	}

}