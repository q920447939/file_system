/**
 * @Project:
 * @Author: leegoo
 * @Date: 2021年11月16日
 */
package cn.withmes.file.system;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: FileController
 *
 * @author leegoo
 * @Description:
 * @date 2021年11月16日
 */
@Controller
@CrossOrigin(allowCredentials = "true", originPatterns = "*", maxAge = 3600)
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @javax.annotation.Resource
    private FileProperties fileProperties; // 文件在本地存储的地址


    @GetMapping(value = "/admin/index")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public String index() {
        return "template/index";
    }


    @PostMapping("/uploadFile")
    @ResponseBody
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }


    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/listFileNames")
    @ResponseBody
    public List<FileItem> listFileNames() {
        Path path = Paths.get(fileProperties.getUploadDir()).toAbsolutePath().normalize();
        File file = path.toFile();
        File[] files = file.listFiles();
        if (null == files) {
            return Collections.emptyList();
        }
        List<File> fileList = Arrays.stream(files).sorted((a, b) -> (int) (b.lastModified() - a.lastModified())).collect(Collectors.toList());
        List<FileItem> result = new ArrayList<>(files.length);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
        for (File itemFile : fileList) {
            long lastModified = itemFile.lastModified();
            double d = itemFile.length() / 1048576D;
            result.add(FileItem.builder().filename(itemFile.getName()).freeSpace(String.format("%.2f", d)).lastModifiedStr(format.format(lastModified)).build());
        }
        return result;
    }


    @GetMapping("/deleteFile/{fileName}")
    @ResponseBody
    public boolean deleteFile(@PathVariable("fileName") String fileName) {
        return fileService.deleteFile(fileName);
    }


    @GetMapping("/404")
    public String notFoundPage() {
        return "404";
    }

    @GetMapping("/403")
    public String accessError() {
        return "403";
    }

    @GetMapping("/500")
    public String internalError() {
        return "500";
    }

    @Setter
    @Getter
    @Builder
    static
    class FileItem implements Serializable {
        private String filename;
        private long lastModifiedL;
        private String lastModifiedStr;
        private String freeSpace;
    }



}
