package ro.unibuc.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ro.unibuc.hello.data.InformationEntity;
import ro.unibuc.hello.data.InformationRepository;

import jakarta.annotation.PostConstruct;
import ro.unibuc.hello.data.CategoryRepository;
import ro.unibuc.hello.data.FurnitureRepository;
import ro.unibuc.hello.data.SupplierRepository;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = {InformationRepository.class, FurnitureRepository.class, CategoryRepository.class, SupplierRepository.class})
public class HelloApplication {

	@Autowired
	private InformationRepository informationRepository;

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}

	@PostConstruct
	public void runAfterObjectCreated() {
		informationRepository.deleteAll();
		informationRepository.save(new InformationEntity("Overview",
				"This is an example of using a data storage engine running separately from our applications server"));
	}

}
