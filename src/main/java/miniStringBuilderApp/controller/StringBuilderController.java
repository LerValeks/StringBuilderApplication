package miniStringBuilderApp.controller;


import miniStringBuilderApp.exceptions.UserRequestException;
import miniStringBuilderApp.model.StringInput;
import miniStringBuilderApp.service.StringBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class StringBuilderController {

    @Autowired
    private StringBuilderService stringBuilderService;

    @PostMapping("/string-input")
    public ResponseEntity inputNewStringPermutationJob(@RequestBody StringInput stringInput) throws UserRequestException {

        if (stringInput.getUserInput().length() < stringInput.getMinLength()) {
            throw new UserRequestException("Number of charecters in InputString is less then minum length");
        } else if (stringInput.getMinLength() == 0 || stringInput.getMaxLength() == 0) {
            throw new UserRequestException("Minimum and Maximum length can't be 0");
        } else if (stringInput.getMinLength() > stringInput.getMaxLength()) {
            throw new UserRequestException("Maximum length can't be lower than minimum length");
        }


        StringInput stringInput1 = stringBuilderService.createFileFromString(stringInput);

        return new ResponseEntity<>(stringInput1, HttpStatus.CREATED);
    }

    @GetMapping("/all-files")
    public ResponseEntity<List> getAllStringPermutationJobs() throws EntityNotFoundException {
        return new ResponseEntity<List>((List) stringBuilderService.getAllStringInputFile(), HttpStatus.OK);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<String> getFile(@PathVariable Long id) throws EntityNotFoundException {
        StringInput stringInput = stringBuilderService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + stringInput.getUserInput() + ".txt\"")
                .body(stringInput.getStringPermutationFile());
    }

}
