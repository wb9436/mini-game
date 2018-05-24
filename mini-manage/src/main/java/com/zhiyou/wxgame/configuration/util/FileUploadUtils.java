package com.zhiyou.wxgame.configuration.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.zhiyou.wxgame.util.utils.RandStrUtils;
import com.zhiyou.wxgame.util.utils.StringUtils;

public class FileUploadUtils {
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 文件上传
	 * 
	 * @param uploadPath 上传地址
	 * @param multipartFile
	 * @param filrUrl
	 */
	protected void doFileUpload(String uploadPath, MultipartFile multipartFile, String filrUrl) {
		try {
			File destFile = new File(uploadPath, filrUrl);
			if (!destFile.getParentFile().exists()) { // 判断父目录是否存在,如果父目录不存在，则创建目录
				destFile.getParentFile().mkdir();
			}
			multipartFile.transferTo(destFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件copy
	 * 
	 * @param uploadPath 上传地址
	 * @param file
	 * @param filrUrl
	 */
	protected void doFileCopy(String uploadPath, File file, String filrUrl) {
		try {
			File destFile = new File(uploadPath, filrUrl);
			if (!destFile.isDirectory()) {
				destFile.createNewFile();
			}
			FileUtils.copyFile(file, destFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param uploadPath 上传地址
	 * @param oldFileUrl
	 */
	protected void doDeleteOldFile(String uploadPath, String oldFileUrl) {
		try {
			if (!StringUtils.isEmpty(oldFileUrl)) {
				File file = new File(uploadPath, oldFileUrl);
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传图片
	 * 
	 * @param uploadPath 上传地址
	 * @param oldFileUrl 旧图片地址
	 * @param urlPrefix 上传目录前缀
	 * @param imageId 图片编号
	 * @param iconFile
	 * @return
	 */
	public String uploadImage(String uploadPath, String oldFileUrl, String urlPrefix, String imageId,
			MultipartFile iconFile) {
		if (iconFile == null) {
			return null;
		}
		try {
			String fileName = iconFile.getOriginalFilename();
			if (!StringUtils.isEmpty(fileName)) {
				String url = urlPrefix + "/" + imageId + "_" + RandStrUtils.randNumeric(6)
						+ fileName.substring(fileName.indexOf("."));
				this.doFileUpload(uploadPath, iconFile, url);
				this.doDeleteOldFile(uploadPath, oldFileUrl);
				return url;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
