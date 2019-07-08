package com.helpdesk.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.helpdesk.entity.Equipment;
import com.helpdesk.exception.ResponseNotFoundException;
import com.helpdesk.service.EquipmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/equipments")
@Api(value = "REST service for equipments")
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;

	@ApiOperation("Return equipment by id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Equipment> findById(@PathVariable("id") Integer id) {
		Optional<Equipment> equipment = equipmentService.findById(id);
		if (!equipment.isPresent()) {
			throw new ResponseNotFoundException("ID: " + id);
		}

		return new ResponseEntity<Equipment>(equipment.get(), HttpStatus.OK);
	}
	
	@GetMapping(value="/pageable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Equipment>> listarPageable(Pageable pageable){
		Page<Equipment> equipments;
		equipments = equipmentService.listarPageable(pageable);
		return new ResponseEntity<Page<Equipment>>(equipments, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Equipment> registrar(@Valid @RequestBody Equipment equipment){
		Equipment equipmentNew = new Equipment();
		equipmentNew = equipmentService.registrar(equipment);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(equipmentNew.getId()).toUri();
		return ResponseEntity.created(location).build();		
	}
	
	@ApiOperation("Update equipment")
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> update(@Valid @RequestBody Equipment equipment) {
		equipmentService.update(equipment);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation("Delete user by id")
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> delete(@PathVariable("id") Integer id){
		Optional<Equipment> equipment = equipmentService.findById(id);
		
		if (!equipment.isPresent()) {
			throw new ResponseNotFoundException("ID: " + id);
		}else {
			equipmentService.deleteById(id);
			return new ResponseEntity<>("Equipment has been deleted!", HttpStatus.OK);
		}
	}
	
	@ApiOperation("Delete all equipments")
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteAll(){
		equipmentService.deleteAll();
		return new ResponseEntity<>("All equipments has been deleted!",HttpStatus.OK);
	}
	
	@ApiOperation("Return user by type equipment id")
	@GetMapping(value = "/type/{id}")
	public ResponseEntity<List<Equipment>> findByIdEquipmentType(@PathVariable("id") Integer id){
		
		List<Equipment> equip = equipmentService.finByIdEquipmentType(id);
		return new ResponseEntity<List<Equipment>>(equip,HttpStatus.OK);
	}

}
