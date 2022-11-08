package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	private UserRepository repository;
	private PostRepository postRepository;
	
	public UserJpaResource(UserRepository repository, PostRepository postRepository) {
		this.repository=repository;
		this.postRepository = postRepository;
	}
	
	@GetMapping(path = "/jpa/users")
	public List<User> retrieveAllUsers() {
		return repository.findAll();
	}
	
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retriveUser(@PathVariable Integer id) {
		Optional<User> user = repository.findById(id);
		
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:" + id);
		}
		
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		return entityModel;
	}
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable Integer id) {
		repository.deleteById(id);
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrivePostsForUser(@PathVariable Integer id) {
		Optional<User> user = repository.findById(id);
		
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:"+id);
		}
		
		return user.get().getPosts();
	}
	
	@GetMapping("/jpa/users/{id}/posts/{pid}")
	public EntityModel<Post> retrivePostById(@PathVariable Integer id, @PathVariable Integer pid) throws PostNotFoundException {
		Optional<Post> post = postRepository.findById(pid);
		
		if (post.isEmpty()) {
			throw new PostNotFoundException("id:"+id);
		}
		
		EntityModel<Post> entityModel = EntityModel.of(post.get());
		
		WebMvcLinkBuilder userLink = linkTo(methodOn(this.getClass()).retriveUser(id));
		entityModel.add(userLink.withRel("user"));
		
		WebMvcLinkBuilder userPostsLink = linkTo(methodOn(this.getClass()).retrivePostsForUser(id));
		entityModel.add(userPostsLink.withRel("user-posts"));
		
		return entityModel;
	}
	
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		User savedUser = repository.save(user);
		
		// location below is holding the path of the new user created
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // will retrun the path of the current request
				.path("/{id}") // will append a placeholder to the request
				.buildAndExpand(savedUser.getId()) // will replace the placeholder with this value
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> createPostsForUser(@PathVariable Integer id, @Valid @RequestBody Post post) {
		Optional<User> user = repository.findById(id);
		
		if (user.isEmpty()) {
			throw new UserNotFoundException("id:"+id);
		}
		
		post.setUser(user.get());
		Post savedPost = postRepository.save(post);
		
		// location below is holding the path of the new user created
		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // will retrun the path of the current request
				.path("/{id}") // will append a placeholder to the request
				.buildAndExpand(savedPost.getId()) // will replace the placeholder with this value
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
}
