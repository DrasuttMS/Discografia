
package org.iplacex.proyectos.discografia.artistas;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ArtistaController {

    private final IArtistaRepository artistaRepository;

    public ArtistaController(IArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    @PostMapping(
        value = "/artista",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleInsertArtistaRequest(
            @RequestBody Artista artista) {

        try {
            Artista nuevo = artistaRepository.insert(artista);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevo);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar el artista.");
        }
    }

    @GetMapping(
        value = "/artistas",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Artista>> HandleGetAristasRequest() {

        return ResponseEntity.ok(artistaRepository.findAll());
    }

    @GetMapping(
        value = "/artista/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleGetArtistaRequest(
            @PathVariable String id) {

        Optional<Artista> artista = artistaRepository.findById(id);

        if (artista.isPresent()) {
            return ResponseEntity.ok(artista.get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("No se encontró el artista solicitado.");
    }

    @PutMapping(
        value = "/artista/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleUpdateArtistaRequest(
            @PathVariable String id,
            @RequestBody Artista artista) {

        try {
            if (!artistaRepository.existsById(id)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe el artista que desea actualizar.");
            }

            artista._id = id;

            Artista actualizado = artistaRepository.save(artista);

            return ResponseEntity.ok(actualizado);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el artista.");
        }
    }

    @DeleteMapping(
        value = "/artista/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> HandleDeleteArtistaRequest(
            @PathVariable String id) {

        try {
            if (!artistaRepository.existsById(id)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No existe el artista que desea eliminar.");
            }

            artistaRepository.deleteById(id);

            return ResponseEntity.ok("Artista eliminado correctamente.");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el artista.");
        }
    }
}
