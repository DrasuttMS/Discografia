
package org.iplacex.proyectos.discografia.discos;

import java.util.List;
import java.util.Optional;

import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DiscoController {

    private final IDiscoRepository discoRepository;
    private final IArtistaRepository artistaRepository;

    public DiscoController(
            IDiscoRepository discoRepository,
            IArtistaRepository artistaRepository) {

        this.discoRepository = discoRepository;
        this.artistaRepository = artistaRepository;
    }

    // Registrar un disco
    @PostMapping(
        value = "/disco",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandlePostDiscoRequest(
            @RequestBody Disco disco) {

        try {
            if (!artistaRepository.existsById(disco.idArtista)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("El artista asociado no existe.");
            }

            Disco nuevoDisco = discoRepository.insert(disco);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevoDisco);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar el disco.");
        }
    }

    // Obtener todos los discos
    @GetMapping(
        value = "/discos",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetDiscosRequest() {

        List<Disco> discos = discoRepository.findAll();

        return ResponseEntity.ok(discos);
    }

    // Obtener un disco por ID
    @GetMapping(
        value = "/disco/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetDiscoRequest(
            @PathVariable String id) {

        Optional<Disco> disco = discoRepository.findById(id);

        if (disco.isPresent()) {
            return ResponseEntity.ok(disco.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("No se encontró el disco solicitado.");
    }

    // Obtener discos asociados a un artista
    @GetMapping(
        value = "/artista/{id}/discos",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetDiscosByArtistaRequest(
            @PathVariable String id) {

        if (!artistaRepository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el artista solicitado.");
        }

        List<Disco> discos =
                discoRepository.findDiscosByIdArtista(id);

        return ResponseEntity.ok(discos);
    }
}
