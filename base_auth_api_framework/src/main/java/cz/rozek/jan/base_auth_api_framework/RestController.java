package cz.rozek.jan.base_auth_api_framework;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mongodb.DuplicateKeyException;

import cz.rozek.jan.base_auth_api_framework.exceptions.MissedAuthExeption;
import cz.rozek.jan.base_auth_api_framework.exceptions.SecurityException;
import cz.rozek.jan.base_auth_api_framework.exceptions.ValidationException;
import cz.rozek.jan.base_auth_api_framework.services.transactions.ICrudTransactionService;

public abstract class RestController<E extends Entity> {

    protected ICrudTransactionService<E,?> service;

    public abstract void setService(ICrudTransactionService<E, ?> service);

    @PostMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<List<E>> getAll(@RequestHeader Map<String, String> headers, @RequestBody E example, @PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            List<E> entities = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .readAll(example, pageable);

            return new ResponseEntity<>(entities, HttpStatus.OK); 
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 

    @GetMapping("/{id}")
    public ResponseEntity<E> getOne(@PathVariable String id, @RequestHeader Map<String,String> headers) {
        try {
            E entity = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .readById(id);

            return new ResponseEntity<>(entity, HttpStatus.OK);
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/")
    public ResponseEntity<Object> post(@RequestBody E data, @RequestHeader Map<String,String> headers) {
        try {    
            E saved = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .create(data);

            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ValidationException | DuplicateKeyException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/many")
    public ResponseEntity<Object> postMany(@RequestBody List<E> data, @RequestHeader Map<String, String> headers) {
        try {    
            List<E> saved = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .createMany(data);

            return new ResponseEntity<>(saved, HttpStatus.OK);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ValidationException | DuplicateKeyException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@PathVariable String id, @RequestBody E data, @RequestHeader Map<String,String> headers) {
        try {
            E updated = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .update(id, data);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/many")
    public ResponseEntity<Object> putMany(@RequestBody Map<String, E> data, @RequestHeader Map<String, String> headers) {
        try {
            List<E> updated = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .updateMany(data);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (ValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id, @RequestHeader Map<String,String> headers) {
        try {
            E deleted = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .delete(id);

            if (deleted != null) 
                return new ResponseEntity<>(deleted, HttpStatus.OK);
            else 
                throw new NullPointerException();   
        } catch (NullPointerException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{limit}")
    public ResponseEntity<List<E>> deleteByExample(@RequestHeader Map<String,String> headers, @RequestBody E example, @PathVariable Integer limit) {
        try {
            List<E> deleted = service.createSecuredTransaction()
                .setJWT(headers.get(HeaderItems.AUTHORIZATION))
                .deleteByExample(example, limit);

            return new ResponseEntity<>(deleted, HttpStatus.OK); 
        } catch (NullPointerException | NoSuchElementException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (MissedAuthExeption e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
