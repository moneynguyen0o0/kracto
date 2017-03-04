package com.kracto.web.controller.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kracto.util.SecurityUtils;
import com.kracto.web.constant.URL;
import com.kracto.web.constant.View;

@Controller
@RequestMapping(URL.ADMIN_IMAGE_FOLDER)
public class ImageFolderController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageFolderController.class);

	private final static String IMAGE_FOLDER = "/resources/images";

	@Autowired
	private ServletContext context;
	@Autowired
	private MessageSource messages;

	@GetMapping
	public String imageFolder() {
		return View.ADMIN_IMAGE_FOLDER;
	}

	@PostMapping
	public String subImageFolder(HttpServletRequest request, Model model) {
		getImageFolder(request, model, null);
		return View.ADMIN_SUB_IMAGE_FOLDER;
	}

	private void getImageFolder(HttpServletRequest request, Model model, String curentFolder) {
		List<String> imageNames = new ArrayList<String>();
		List<String> folderNames = new ArrayList<String>();
		String folderName = null;
		if (curentFolder != null)
			folderName = curentFolder;
		else
			folderName = request.getParameter("folderName");
		if (folderName == null)
			folderName = "";
		String path = context.getRealPath(IMAGE_FOLDER + folderName);
		File folder = new File(path);
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				folderNames.add(file.getName());
			} else if (isImage(file)) {
				imageNames.add(file.getName());
			}
		}
		boolean flag = false;
		if (folderName.split("/").length == 3)
			flag = true;
		model.addAttribute("folderNames", folderNames);
		model.addAttribute("imageNames", imageNames);
		model.addAttribute("subUrl", folderName);
		model.addAttribute("flag", flag);
	}

	@PostMapping(URL.ADD_FOLDER)
	public String addImage(HttpServletRequest request, Model model) {
		String folderName = request.getParameter("inputFolder");
		String curentFolder = request.getParameter("curentFolder");
		if (folderName != null && !folderName.isEmpty()) {
			File dir = new File(getRoot() + curentFolder + File.separator + folderName);
			if (!dir.exists()) {
				dir.mkdir();
				
				LOGGER.info("ADMIN {} - Added folder {}", SecurityUtils.getUsername(), folderName);
			} else {
				model.addAttribute("message", messages.getMessage("imagefolder.existed", null, request.getLocale()));
			}
		}
		getImageFolder(request, model, curentFolder);
		return View.ADMIN_SUB_IMAGE_FOLDER;
	}

	@PostMapping(URL.ADD_IMAGE)
	public String addImage(@RequestParam("images") MultipartFile[] images, HttpServletRequest request, Model model) {
		String curentFolder = request.getParameter("curentFolder");
		if (images != null && images.length > 0) {
			for (int i = 0; i < images.length; i++) {
				if (!images[i].isEmpty()) {
					try {
						String file = getRoot() + curentFolder + File.separator + (new Date()).getTime()
								+ "." + FilenameUtils.getExtension(images[i].getOriginalFilename());
						images[i].transferTo(new File(file));
						
						LOGGER.info("ADMIN {} - Added image {}", SecurityUtils.getUsername(), file);
					} catch (IllegalStateException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		getImageFolder(request, model, curentFolder);
		return View.ADMIN_SUB_IMAGE_FOLDER;
	}

	@PostMapping(URL.DELETE)
	public String deleteFolderImage(HttpServletRequest request, Model model) {
		String fileName = request.getParameter("fileName");
		String curentFolder = request.getParameter("curentFolder");
		File file = new File(getRoot() + curentFolder + File.separator + fileName);
		if (file.isDirectory()) {
			try {
				FileUtils.deleteDirectory(file);
				
				LOGGER.info("ADMIN {} - Deleted folder {}", SecurityUtils.getUsername(), file);
			} catch (IOException e) {
				model.addAttribute("message", messages.getMessage("imagefolder.delete.not", null, request.getLocale()));
				e.printStackTrace();
			}
		} else {
			try {
				FileUtils.forceDelete(file);
				
				LOGGER.info("ADMIN {} - Deleted image {}", SecurityUtils.getUsername(), file);
			} catch (IOException e) {
				model.addAttribute("message", messages.getMessage("imagefolder.delete.not", null, request.getLocale()));
				e.printStackTrace();
			}
		}
		getImageFolder(request, model, curentFolder);
		return View.ADMIN_SUB_IMAGE_FOLDER;
	}

	private boolean isImage(File file) {
		try {
			Image image = ImageIO.read(file);
			if (image != null)
				return true;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getRoot() {
		return context.getRealPath("/") + IMAGE_FOLDER + File.separator;
	}
	
}
