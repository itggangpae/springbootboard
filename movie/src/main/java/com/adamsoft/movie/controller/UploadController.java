package com.adamsoft.movie.controller;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.adamsoft.movie.dto.UploadResultDTO;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class UploadController {
	//application.properties 파일에 있는 com.adamsoft.upload.path 라는 속성에 설정된 값 가져오기
	@Value("${com.adamsoft.upload.path}")
	private String uploadPath;
	
	private String makeFolder() {
		//오늘 날짜를 문자열로 가져옴
		String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		// /문자열을 파일의 경로 구분자로 변경
		String realUploadPath = str.replace("//", File.separator);
		//uploadPath 와  realUploadPath를 합쳐서 파일 객체를 생성
		File uploadPathDir = new File(uploadPath, realUploadPath);
		//이 파일이 존재하지 않는다면 디렉토리를 생성
		if(uploadPathDir.exists() == false) {
			uploadPathDir.mkdirs();
		}
		//디렉토리 이름을 리턴
		return realUploadPath;
	}
	
	//이미지 업로드 처리
	@PostMapping(value="/uploadajax")
	public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
		//리턴할 List 생성
		List<UploadResultDTO> resultDTOList = new ArrayList<>();
		
		//log.info("uploadPath:" + uploadPath);
		for(MultipartFile uploadFile : uploadFiles) {
			//이미지 파일이 아닌 파일이 있으면 업로드 중지
			if(uploadFile.getContentType().startsWith("image") == false) {
				log.warn("이미지 파일만 업로드 하세요");
				//실패 상태를 전송합니다.
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			String originalName = uploadFile.getOriginalFilename();
			String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
			log.info("fileName:" + fileName);
			
			//파일을 저장할 디렉토리 생성
			String realUploadPath = makeFolder();
			
			//UUID 생성
			String uuid = UUID.randomUUID().toString();
			//실제 저장할 파일 경로를 생성
			String saveName = uploadPath + File.separator + realUploadPath + 
					File.separator + uuid + fileName;
			Path savePath = Paths.get(saveName);
			try {
				//파일 업로드
				uploadFile.transferTo(savePath);
				
				resultDTOList.add(new UploadResultDTO(fileName, uuid, realUploadPath));
			}catch(Exception e) {
				System.out.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
		return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
	}
	
	//파일의 내용을 전송하는 요청을 처리해주는 메서드
	@GetMapping("/display")
	public ResponseEntity<byte []> getFile(String filename){
		log.info("display:" + filename);
		ResponseEntity <byte []> result = null;
		try {
			//파일 이름을 가지고 파일 경로를 생성
			File file = new File(uploadPath + File.separator + 
					URLDecoder.decode(filename, "UTF-8"));
			//Header 는 데이터의 종류가 어떤 것인지 알려주기 위한 용도
			HttpHeaders  header = new HttpHeaders();
			//Content-Type에 이 파일의 종류가 무엇인지 설정을 해줍니다.
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, 
					HttpStatus.OK);
			
		}catch(Exception e) {
			log.error(e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
	
	
	
	
	
	
	
}
