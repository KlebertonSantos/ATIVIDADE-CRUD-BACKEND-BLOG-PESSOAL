package com.generation.blogpessoal.Controller;

import com.generation.blogpessoal.Repository.PostagemRepository;
import com.generation.blogpessoal.Repository.TemaRepository;
import com.generation.blogpessoal.model.Postagem;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class PostagemController {
    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private TemaRepository temaRepository;

    @GetMapping
    public ResponseEntity<List<Postagem>> getAll(){

        return ResponseEntity.ok(postagemRepository.findAll());
    }
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
        return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
    }

    @PostMapping
    public ResponseEntity<Postagem>Post(@Valid @RequestBody Postagem postagem){
        if(temaRepository.existsById(postagem.getTema().getId()))
            return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping
    public ResponseEntity<Postagem> Put(@Valid @RequestBody Postagem postagem){
        if(postagemRepository.existsById(postagem.getId())){
            if (temaRepository.existsById(postagem.getTema().getId()))
                return  ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return postagemRepository.findById(postagem.getId()).map(resposta -> ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem))).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        Optional<Postagem> postagem = postagemRepository.findById(id);

         if(postagem.isEmpty())
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
         postagemRepository.deleteById(id);
    }






}
