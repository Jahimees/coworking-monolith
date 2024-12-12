package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Office;
import by.bsuir.antonovich.backend.repository.OfficeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;

    public List<Office> findAll() {
        return officeRepository.findAll();
    }

    public Office create(Office office) {
        if (office == null) {
            throw new IllegalArgumentException("Office is null");
        }

        return officeRepository.save(office);
    }

    public Office updateOfficeInfo(Office office) {
        if (office == null) {
            throw new IllegalArgumentException("Office is null");
        }

        Optional<Office> dbOfficeOptional = findById(office.getId());

        if (dbOfficeOptional.isEmpty()) {
            throw new IllegalArgumentException("Office not found");
        }

        Office dbOffice = dbOfficeOptional.get();

        dbOffice.setName(office.getName());
        dbOffice.setAddress(office.getAddress());
        return officeRepository.save(dbOffice);
    }

    public Optional<Office> findById(Integer id) {
        return officeRepository.findById(id);
    }

    @Transactional
    public void deleteById(Integer id) {

        officeRepository.deleteById(id);
//        TODO booking deletion
        //delete floor
        //delete rooms
    }
}
