package io.vividcode.happyride.passengerservice.web;

import io.vividcode.happyride.passengerservice.api.web.CreatePassengerRequest;
import io.vividcode.happyride.passengerservice.api.web.CreateUserAddressRequest;
import io.vividcode.happyride.passengerservice.api.web.PassengerVO;
import io.vividcode.happyride.passengerservice.api.web.UserAddressVO;
import io.vividcode.happyride.passengerservice.domain.PassengerService;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1")
public class PassengerController {

  @Autowired
  PassengerService passengerService;

  @GetMapping
  public List<PassengerVO> findAll() {
    return passengerService.findAll();
  }

  @GetMapping("{id}")
  public ResponseEntity<PassengerVO> getPassenger(@PathVariable("id") String passengerId) {
    return passengerService.getPassenger(passengerId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<PassengerVO> createPassenger(
      @RequestBody CreatePassengerRequest request) {
    PassengerVO passenger = passengerService.createPassenger(request);
    return ResponseEntity.created(resourceCreated(passenger.getId())).body(passenger);
  }

  @PostMapping("{id}/addresses")
  public ResponseEntity<PassengerVO> createAddress(@PathVariable("id") String passengerId,
      @RequestBody CreateUserAddressRequest request) {
    PassengerVO passenger = passengerService.addAddress(passengerId, request);
    return ResponseEntity.ok(passenger);
  }

  @DeleteMapping("{passengerId}/addresses/{addressId}")
  public ResponseEntity<Void> deleteAddress(@PathVariable("passengerId") String passengerId,
      @PathVariable("addressId") String addressId) {
    passengerService.deleteAddress(passengerId, addressId);
    return ResponseEntity.noContent().build();
  }

  private URI resourceCreated(String resourceId) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();
  }
}
