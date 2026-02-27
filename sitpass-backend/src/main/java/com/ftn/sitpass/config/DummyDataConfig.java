package com.ftn.sitpass.config;

import com.ftn.sitpass.model.FacilityDocument;
import com.ftn.sitpass.model.AccountRequest;
import com.ftn.sitpass.model.Discipline;
import com.ftn.sitpass.model.Exercise;
import com.ftn.sitpass.model.Facility;
import com.ftn.sitpass.model.Image;
import com.ftn.sitpass.model.Rate;
import com.ftn.sitpass.model.RequestStatus;
import com.ftn.sitpass.model.Review;
import com.ftn.sitpass.model.User;
import com.ftn.sitpass.model.WorkDay;
import com.ftn.sitpass.repository.AccountRequestRepository;
import com.ftn.sitpass.repository.DisciplineRepository;
import com.ftn.sitpass.repository.ExerciseRepository;
import com.ftn.sitpass.repository.FacilityDocumentRepository;
import com.ftn.sitpass.repository.FacilityRepository;
import com.ftn.sitpass.repository.ImageRepository;
import com.ftn.sitpass.repository.RateRepository;
import com.ftn.sitpass.repository.ReviewRepository;
import com.ftn.sitpass.repository.UserRepository;
import com.ftn.sitpass.repository.WorkDayRepository;
import com.ftn.sitpass.service.ues.FacilityUesService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class DummyDataConfig {
    private static final String[] LOCAL_GYM_IMAGES = new String[] {
            "http://localhost:8080/images/gym-1.jpg",
            "http://localhost:8080/images/gym-2.jpg",
            "http://localhost:8080/images/gym-3.jpg"
    };

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final DisciplineRepository disciplineRepository;
    private final WorkDayRepository workDayRepository;
    private final ExerciseRepository exerciseRepository;
    private final RateRepository rateRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final AccountRequestRepository accountRequestRepository;
    private final FacilityDocumentRepository facilityDocumentRepository;
    private final FacilityUesService facilityUesService;

    @Bean
    @Order(1)
    @Transactional
    public CommandLineRunner seedDummyData() {
        return args -> {
            ensureUser("admin@sitpass.rs", "admin123", "Admin", "System",
                    "+38160111111", LocalDate.of(1990, 1, 1), "Bulevar Oslobodjenja 1");
            User userOne = ensureUser("nikola@example.com", "test123", "Nikola", "Nikolic",
                    "+381601234567", LocalDate.of(1998, 3, 12), "Kralja Petra 10");
            User userTwo = ensureUser("marija@example.com", "test123", "Marija", "Markovic",
                    "+381601987654", LocalDate.of(1999, 8, 23), "Cara Dusana 22");

            Facility missFit = ensureFacility("Miss Fit",
                    "Moderan fitness centar sa grupnim i individualnim treninzima.",
                    "Futoska 12");
            Facility tCentrala = ensureFacility("Teretana Centrala",
                    "Prostor za snagu i kondiciju sa celodnevnim radnim vremenom.",
                    "Jevrejska 8");
            Facility yogaHub = ensureFacility("Yoga Hub",
                    "Studio za yogu i pilates, idealan za pocetnike i napredne.",
                    "Bulevar Evrope 45");

            addDisciplines(missFit, "Gym", "Crossfit", "Cardio");
            addDisciplines(tCentrala, "Gym", "Powerlifting");
            addDisciplines(yogaHub, "Yoga", "Pilates", "Stretching");

            addWorkDaysForWholeWeek(missFit, LocalTime.of(6, 0), LocalTime.of(23, 0));
            addWorkDaysForWholeWeek(tCentrala, LocalTime.of(7, 0), LocalTime.of(22, 0));
            addWorkDaysForWholeWeek(yogaHub, LocalTime.of(8, 0), LocalTime.of(21, 0));

            addFacilityImagesIfMissing(missFit);
            addFacilityDocumentIfMissing(missFit,
                    "Miss Fit je moderan fitness centar u Novom Sadu sa grupnim i individualnim treninzima.");
            addFacilityDocumentIfMissing(tCentrala,
                    "Teretana Centrala nudi program snage, kardio treninge i radno vreme prilagodjeno zaposlenima.");
            addFacilityDocumentIfMissing(yogaHub,
                    "Yoga Hub je studio za yogu i pilates sa fokusom na zdravlje, mobilnost i balans.");
            addFacilityDocumentsForAllFacilitiesIfMissing();

            if (exerciseRepository.count() == 0) {
                Exercise ex1 = exerciseRepository.save(Exercise.builder()
                        .user(userOne)
                        .facility(missFit)
                        .fromTime(LocalDateTime.now().minusDays(12).withHour(18).withMinute(0))
                        .untilTime(LocalDateTime.now().minusDays(12).withHour(19).withMinute(0))
                        .build());

                Exercise ex2 = exerciseRepository.save(Exercise.builder()
                        .user(userOne)
                        .facility(tCentrala)
                        .fromTime(LocalDateTime.now().minusDays(8).withHour(17).withMinute(0))
                        .untilTime(LocalDateTime.now().minusDays(8).withHour(18).withMinute(0))
                        .build());

                Exercise ex3 = exerciseRepository.save(Exercise.builder()
                        .user(userTwo)
                        .facility(yogaHub)
                        .fromTime(LocalDateTime.now().minusDays(6).withHour(19).withMinute(0))
                        .untilTime(LocalDateTime.now().minusDays(6).withHour(20).withMinute(0))
                        .build());

                Rate rateOne = rateRepository.save(Rate.builder().staff(9).equipment(8).hygiene(9).space(8).build());
                Rate rateTwo = rateRepository.save(Rate.builder().staff(8).equipment(9).hygiene(8).space(9).build());
                Rate rateThree = rateRepository.save(Rate.builder().staff(10).equipment(9).hygiene(10).space(9).build());

                reviewRepository.save(Review.builder()
                        .createdAt(ex1.getUntilTime().plusDays(1))
                        .exerciseCount(1)
                        .hidden(false)
                        .author(userOne)
                        .facility(missFit)
                        .rating(rateOne)
                        .build());

                reviewRepository.save(Review.builder()
                        .createdAt(ex2.getUntilTime().plusDays(1))
                        .exerciseCount(1)
                        .hidden(false)
                        .author(userOne)
                        .facility(tCentrala)
                        .rating(rateTwo)
                        .build());

                reviewRepository.save(Review.builder()
                        .createdAt(ex3.getUntilTime().plusDays(1))
                        .exerciseCount(1)
                        .hidden(false)
                        .author(userTwo)
                        .facility(yogaHub)
                        .rating(rateThree)
                        .build());
            }

            if (accountRequestRepository.findByEmail("new.user@sitpass.rs").isEmpty()) {
                accountRequestRepository.save(AccountRequest.builder()
                        .email("new.user@sitpass.rs")
                        .password("changeme")
                        .createdAt(LocalDate.now().minusDays(2))
                        .status(RequestStatus.PENDING)
                        .rejectReason(null)
                        .build());
            }
        };
    }

    private User ensureUser(String email, String password, String name, String surname,
                            String phone, LocalDate birthDate, String address) {
        return userRepository.findAll().stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst()
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .password(password)
                        .name(name)
                        .surname(surname)
                        .createdAt(LocalDate.now().minusMonths(6))
                        .phoneNumber(phone)
                        .birthDay(birthDate)
                        .address(address)
                        .city("Novi Sad")
                        .zipCode("21000")
                        .build()));
    }

    private Facility ensureFacility(String name, String description, String address) {
        return facilityRepository.findAll().stream()
                .filter(facility -> name.equalsIgnoreCase(facility.getName()))
                .findFirst()
                .orElseGet(() -> facilityRepository.save(Facility.builder()
                        .name(name)
                        .description(description)
                        .createdAt(LocalDate.now().minusMonths(4))
                        .address(address)
                        .city("Novi Sad")
                        .totalRating(0.0)
                        .active(true)
                        .build()));
    }

    private void addDisciplines(Facility facility, String... names) {
        for (String name : names) {
            boolean alreadyExists = disciplineRepository.findAll().stream()
                    .anyMatch(discipline -> discipline.getFacility().getId().equals(facility.getId())
                            && name.equalsIgnoreCase(discipline.getName()));
            if (!alreadyExists) {
                disciplineRepository.save(Discipline.builder()
                        .name(name)
                        .facility(facility)
                        .build());
            }
        }
    }

    private void addWorkDaysForWholeWeek(Facility facility, LocalTime from, LocalTime until) {
        for (DayOfWeek day : DayOfWeek.values()) {
            boolean alreadyExists = workDayRepository.findAll().stream()
                    .anyMatch(workDay -> workDay.getFacility().getId().equals(facility.getId())
                            && workDay.getDay() == day);
            if (!alreadyExists) {
                workDayRepository.save(WorkDay.builder()
                        .facility(facility)
                        .day(day)
                        .fromTime(from)
                        .untilTime(until)
                        .validFrom(LocalDate.now().minusMonths(6))
                        .build());
            }
        }
    }

    private void addFacilityImagesIfMissing(Facility facility) {
        var existingImages = imageRepository.findAllByFacilityId(facility.getId());
        if (existingImages.size() >= 2) {
            for (int i = 0; i < existingImages.size(); i++) {
                Image image = existingImages.get(i);
                if (image.getPath() == null || image.getPath().contains("picsum.photos")) {
                    image.setPath(LOCAL_GYM_IMAGES[i % LOCAL_GYM_IMAGES.length]);
                    imageRepository.save(image);
                }
            }
            return;
        }
        imageRepository.save(Image.builder()
                .facility(facility)
                .path(LOCAL_GYM_IMAGES[0])
                .objectKey("dummy/missfit-1")
                .originalFileName("missfit-1.jpg")
                .contentType("image/jpeg")
                .build());
        imageRepository.save(Image.builder()
                .facility(facility)
                .path(LOCAL_GYM_IMAGES[1])
                .objectKey("dummy/missfit-2")
                .originalFileName("missfit-2.jpg")
                .contentType("image/jpeg")
                .build());
    }

    private void addFacilityDocumentIfMissing(Facility facility, String text) {
        if (facilityDocumentRepository.findByFacilityId(facility.getId()).isPresent()) {
            return;
        }
        String fileName = facility.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-") + ".pdf";
        MultipartFile pdf = new InMemoryMultipartFile(
                "document",
                fileName,
                "application/pdf",
                createPdfBytes("Sitpass Facility Description", text)
        );
        facilityUesService.uploadAssets(facility.getId(), Collections.emptyList(), pdf);
    }

    private void addFacilityDocumentsForAllFacilitiesIfMissing() {
        facilityRepository.findAll().forEach(facility -> {
            String text = String.format(
                    "%s se nalazi na adresi %s, grad %s. %s",
                    facility.getName(),
                    facility.getAddress(),
                    facility.getCity(),
                    facility.getDescription()
            );
            addFacilityDocumentIfMissing(facility, text);
        });
    }

    private byte[] createPdfBytes(String title, String body) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.newLineAtOffset(72, 720);
                contentStream.showText(title);
                contentStream.endText();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(72, 690);
                contentStream.showText(body);
                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to generate dummy PDF content.", e);
        }
    }

    private static class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        private InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public ByteArrayInputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException {
            java.nio.file.Files.write(dest.toPath(), content);
        }
    }
}
