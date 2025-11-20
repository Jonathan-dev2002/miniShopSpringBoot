package com.miniProject.miniShop.service;

import com.miniProject.miniShop.dto.AddressDto;
import com.miniProject.miniShop.model.Address;
import com.miniProject.miniShop.model.User;
import com.miniProject.miniShop.repository.AddressRepository;
import com.miniProject.miniShop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    public Address createAddress(String email, AddressDto request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(request.getIsDefault() )) {
            unsetDefaultAddress(user.getId());
        }

        Address address = new Address();
        address.setUser(user);
        mapDtoToAddress(request, address);

        return addressRepository.save(address);
    }

    public List<Address> getAddressesByUserId(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return addressRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public Address getAddressById(UUID id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address not found or access denied"));
    }

    @Transactional
    public Address updateAddress(UUID id, String email, AddressDto request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Address address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(request.getIsDefault()) && !address.getIsDefault()) {
            unsetDefaultAddress(user.getId());
        }

        mapDtoToAddress(request, address);
        return addressRepository.save(address);
    }

    public void deleteAddress(UUID id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Address address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }

    @Transactional
    public Address setDefaultAddress(UUID id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Address address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (address.getIsDefault()) return address;

        unsetDefaultAddress(user.getId());
        address.setIsDefault(true);
        return addressRepository.save(address);
    }

    private void unsetDefaultAddress(UUID userId) {
        List<Address> defaults = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        for (Address addr : defaults) {
            addr.setIsDefault(false);
            addressRepository.save(addr);
        }
    }

    private void mapDtoToAddress(AddressDto dto, Address address) {
        if (dto.getRecipientName() != null) address.setRecipientName(dto.getRecipientName());
        if (dto.getPhone() != null) address.setPhone(dto.getPhone());
        if (dto.getStreet() != null) address.setStreet(dto.getStreet());
        if (dto.getCity() != null) address.setCity(dto.getCity());
        if (dto.getProvince() != null) address.setProvince(dto.getProvince());
        if (dto.getPostalCode() != null) address.setPostalCode(dto.getPostalCode());
        if (dto.getCountry() != null) address.setCountry(dto.getCountry());
        if (dto.getIsDefault() != null) address.setIsDefault(dto.getIsDefault());
        if (dto.getLabel() != null) address.setLabel(dto.getLabel());
    }
}
