package miniStringBuilderApp.service;


import miniStringBuilderApp.exceptions.UserRequestException;
import miniStringBuilderApp.model.StringInput;
import miniStringBuilderApp.repository.StringBuilderRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class StringBuilderService {

    private StringBuilderRepository stringBuilderRepository;

    public StringBuilderService(StringBuilderRepository stringBuilderRepository) {
        this.stringBuilderRepository = stringBuilderRepository;
    }


    public StringInput createFileFromString(StringInput stringInput) throws UserRequestException {
        stringInput.setMaxDistinctPermutation();
        if (stringInput.getMaxDistinctPermutation().compareTo(stringInput.getNumberOfUniqueStringsRequested()) < 0) {
            throw new UserRequestException("Number of requested strings is much higher than possible number of strings. Number of Requested Strings "
                    + stringInput.getNumberOfUniqueStringsRequested() + " Number of possible Unique Strings: " + stringInput.getMaxDistinctPermutation());
        }

        StringInput savedStringInput1 = stringBuilderRepository.save(stringInput);
        Thread newThread = new Thread(() -> {
            calculatePermutation(stringInput);
        });
        newThread.start();
        return savedStringInput1;
    }


    public void calculatePermutation(StringInput stringInput) {
        stringInput.setStringPermutation();
        stringInput.setStringPermutationFile();
        stringInput.setStatus("Finished");
        stringInput.setdownloadUrl();

        //SET GLOBAL max_allowed_packet=1073741824 if file is not upploading
        stringBuilderRepository.save(stringInput);


    }


    public List getAllStringInputFile() throws EntityNotFoundException {
        return (List) stringBuilderRepository.findAll();
    }

    public StringInput getFile(Long id) {
        StringInput stringInput = stringBuilderRepository.findById(id).orElse(null);
        if (stringInput == null) {
            throw new EntityNotFoundException("Request with such id " + id + " doesn't exist");
        }
        return stringBuilderRepository.findById(id).get();
    }


}
