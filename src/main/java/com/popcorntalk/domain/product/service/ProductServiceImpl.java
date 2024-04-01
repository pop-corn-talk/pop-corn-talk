package com.popcorntalk.domain.product.service;

import static com.popcorntalk.global.exception.ErrorCode.NOT_FOUND;

import com.popcorntalk.domain.product.dto.ProductCreateRequestDto;
import com.popcorntalk.domain.product.dto.ProductReadResponseDto;
import com.popcorntalk.domain.product.dto.ProductUpdateRequestDto;
import com.popcorntalk.domain.product.entity.Product;
import com.popcorntalk.domain.product.repository.ProductRepository;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    //상품 등록
    @Override
    @Transactional
    public void createProduct(ProductCreateRequestDto productCreateRequestDto,
        Long userId) {
        User user = findUser(userId);
        //  validateAdmin(user.getRole());
        Product product = Product.createOf(productCreateRequestDto);

        productRepository.save(product);
    }

    //상품 삭제
    @Override
    @Transactional
    public void deleteProduct(Long productId, Long userId) {
        User user = findUser(userId);
        // validateAdmin(user.getRole());
        Product productDelete = findProduct(productId);
        validateDeleteProduct(productDelete);

        productDelete.softDelete();
    }

    //상품 수정
    @Override
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequestDto productUpdateRequestDto,
        Long userId) {
        User user = findUser(userId);
        // validateAdmin(user.getRole());
        Product productUpdate = findProduct(productId);
        validateDeleteProduct(productUpdate);

        productUpdate.update(productUpdateRequestDto);
    }

    //상품 전체조회
    @Override
    @Transactional(readOnly = true)
    public List<ProductReadResponseDto> getProduct(Pageable pageable) {

        return productRepository.findProduct(pageable);
    }

    private void validateDeleteProduct(Product product) {
        if (product.getDeletionStatus() == DeletionStatus.Y) {
            throw new IllegalArgumentException("삭제된 상품입니다.");
        }
    }

    private List<Product> getProductDeleteStatus() {
        return productRepository.findAllByDeletionStatus(DeletionStatus.N);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    private void validateAdmin(UserRoleEnum userRoleEnum) {
        if (userRoleEnum != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("관리자가 권한이 필요합니다.");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    }
}
