package project.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        //파일명 중복을 피하기 위해 uuid 생성
        UUID uuid = UUID.randomUUID();
        //확장자 가져오기.
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        //uuid + 확장자 이름으로 저장파일 이름 생성
        String savedFileName = uuid.toString() + extension;
        //파일이 업로드될 총 경로.
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        //FileOutputStream 클래스는 바이트 단위의 출력을 내보내는 클래스입니다. 생성자에 파일이 업로드될 총 경로 위치와 파일의 이름을 넘겨주고 파일 출력 스트림을 만든다.
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        //파일 데이터 바이트 값을 파일 출력 스트림에 입력한다.
        fos.write(fileData);
        //파일 출력 스트림을 닫고
        fos.close();
        //업로드된 파일의 이름을 반환한다.
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        //파일이 저장된 경로를 이용하여 파일 객체를 생생
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) { //파일이  존재하면
            deleteFile.delete(); //해당 경로에 있는 파일을 삭제.
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
