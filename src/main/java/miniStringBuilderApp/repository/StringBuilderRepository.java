package miniStringBuilderApp.repository;


import miniStringBuilderApp.model.StringInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StringBuilderRepository extends JpaRepository<StringInput, Long> {


}
