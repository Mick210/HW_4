package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    private final static Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Метод: uploadAvatar {}", studentId);
        Student student = studentService.findStudent(studentId);
        Path filePath = Path.of(avatarDir, student.getName() + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream io = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(io, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setData(file.getBytes());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(filePath));
        avatarRepository.save(avatar);
    }


    public void deleteAvatar(long studentId) {
        logger.info("Метод: deleteAvatar {}", studentId);
        avatarRepository.deleteByStudentId(studentId);
    }

    public Avatar findAvatar(long studentId) {
        logger.info("Метод: findAvatar {}", studentId);
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        logger.info("Метод: generateImagePreview");
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);
            int high = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, high, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, high, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String filename) {
        logger.info("Метод: getExtension");
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public Page<Avatar> findAvatarPreviews(Integer pageNumber, Integer size) {
        logger.info("Метод: findAvatarPreviews");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, size);
        return avatarRepository.findAll(pageRequest);
    }

    public List<Avatar> findAll(Integer pageNumber, Integer size) {
        logger.info("Метод: findAll");
        Pageable page = PageRequest.of(pageNumber - 1, size);
        Page<Avatar> result = avatarRepository.findAll(page);
        return result.getContent();
    }
}

