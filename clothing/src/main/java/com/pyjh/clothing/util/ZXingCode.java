package com.pyjh.clothing.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**  
 * @Title:  ZXingCode.java   
 * @Package com.yuqi.pyjh.util   
 * @Description:   制定logo和制定描述的二维码,最后生成网络地址
 * @author: LiAn   
 * @date:   2018年8月14日 下午3:10:58   
 * @version V1.0 
 */  
public class ZXingCode {
	/*
	 * 0xFF000000-->0x:16进制，FF：透明度，000000:颜色
	 */
	private static final int QRCOLOR = 0xFF000000; // 二维码的颜色默认是黑色
	private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色-金色

	private static final int WIDTH = 400; // 二维码宽
	private static final int HEIGHT = 400; // 二维码高

	// 用于设置QR二维码参数
	private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
		private static final long serialVersionUID = 1L;

		{
			put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
			put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码方式
			put(EncodeHintType.MARGIN, 0);
		}
	};

	public static void main(String[] args) throws Exception {
		//String str = qrCodeAddress("http://dns.pengyoujuhui.com/pengyoujuhui.png","http://www.yuqibest.com/wxtp/authorization.html?userId=5&activity_id=1", "彭友聚汇");
		String str = qrCodeAddress("http://dns.pengyoujuhui.com/pengyoujuhui.png",
				"http://www.yuqibest.com/wxtp1/authorization.html?userId=14&activity_id=9",
				"彭友聚汇");
		System.out.println("str:"+str);
	}
	
	/**
	 * 生成带logo的二维码图片的地址
	 * 
	 * @param logoUrl		:二维码中间log的地址
	 * @param content	:二维码内容
	 * @param note		：二维码标题
	 * @return ：二维码最后地址
	 */
	public static String qrCodeAddress(String logoUrl, String content, String note){
		System.out.println("=====logoUrl:"+logoUrl
				+"\n=======content:"+content
				+"\n=======note:"+note);
		 BufferedImage image = drawLogoQRCode(logoUrl, content, note);
		 byte[] bytes = imageToBytes(image, "png");
		 String imageName = "vote/QrCode/" + UUID.randomUUID().toString();
		 String url = null;
		try {
			url = QiniuCloudUtil.put64image(bytes, imageName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return url;
	}

	/**
	 * 生成带logo的二维码图片
	 * 
	 * @param url		:二维码中间log
	 * @param content	:二维码内容
	 * @param note		：二维码标题
	 * @return
	 */
	public static BufferedImage drawLogoQRCode(String logoUrl, String content, String note) {
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try {
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			// 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			BitMatrix bm = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);

			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
				}
			}

			File logoFile = base64ToFile(logoUrl);
			int width = image.getWidth();
			int height = image.getHeight();
			if (logoFile.exists()) {
				// 构建绘图对象
				Graphics2D g = image.createGraphics();
				// 读取Logo图片
				BufferedImage logo = ImageIO.read(logoFile);
				// 开始绘制logo图片
				g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
				g.dispose();
				logo.flush();
			}

			// 自定义文本描述
			if (StringUtils.isNotEmpty(note)) {
				// 新的图片，把带logo的二维码下面加上文字
				BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D outg = outImage.createGraphics();
				// 画二维码到新的面板
				outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
				// 画文字到新的面板
				outg.setColor(Color.BLACK);
				outg.setFont(new Font("楷体", Font.BOLD, 30)); // 字体、字型、字号
				int strWidth = outg.getFontMetrics().stringWidth(note);
				if (strWidth > 399) {
					// //长度过长就截取前面部分
					// 长度过长就换行
					String note1 = note.substring(0, note.length() / 2);
					String note2 = note.substring(note.length() / 2, note.length());
					int strWidth1 = outg.getFontMetrics().stringWidth(note1);
					int strWidth2 = outg.getFontMetrics().stringWidth(note2);
					outg.drawString(note1, 200 - strWidth1 / 2, height + (outImage.getHeight() - height) / 2 + 12);
					BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
					Graphics2D outg2 = outImage2.createGraphics();
					outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
					outg2.setColor(Color.BLACK);
					outg2.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
					outg2.drawString(note2, 200 - strWidth2 / 2,
							outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
					outg2.dispose();
					outImage2.flush();
					outImage = outImage2;
				} else {
					outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12); // 画文字
				}
				outg.dispose();
				outImage.flush();
				image = outImage;
			}

			image.flush();
			// 写入本地
			// ImageIO.write(image, "png", codeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;

	}

	/**
	 * 转换BufferedImage 数据为byte数组
	 *
	 * @param image
	 *            Image对象
	 * @param format
	 *            image格式字符串.如"gif","png"
	 * @return byte数组
	 */
	public static byte[] imageToBytes(BufferedImage bImage, String format) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, format, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 转换byte数组为Image
	 *
	 * @param bytes
	 * @return Image
	 */
	public static Image bytesToImage(byte[] bytes) {
		Image image = Toolkit.getDefaultToolkit().createImage(bytes);
		try {
			MediaTracker mt = new MediaTracker(new Label());
			mt.addImage(image, 0);
			mt.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return image;
	}

	public static File base64ToFile(String url) {
		byte[] btImg = getImageFromNetByUrl(url);
		File file = null;
		String fileName = "./img.png";
		FileOutputStream out = null;
		try {
			// 解码，然后将字节转换为文件
			file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			ByteArrayInputStream in = new ByteArrayInputStream(btImg);
			byte[] buffer = new byte[1024];
			out = new FileOutputStream(file);
			int bytesum = 0;
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				bytesum += byteread;
				out.write(buffer, 0, byteread); // 文件写操作
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 根据地址获得数据的字节流
	 * 
	 * @param strUrl
	 *            网络连接地址
	 * @return
	 */
	public static byte[] getImageFromNetByUrl(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();// 通过输入流获取图片数据
			byte[] btImg = readInputStream(inStream);// 得到图片的二进制数据
			return btImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}