package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.item.Book;
import jpabook.jpashop.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;

    @Autowired OrderService orderService;

    @Autowired OrderRepository orderRepository;

    @Test
    public void orderTest() throws Exception {
        //given
        Member member = createMember();

        Item book = createItem("JPA Book", 10000, 100);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.find(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(20000, getOrder.getTotalPrice(), "주문 가격은 상품 가격 * 상품 수량 이다.");
        assertEquals(98, book.getStockQuantity(), "주문한 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void overCount() throws Exception {
        //given
        Member member = createMember();
        Item book = createItem("JPA Book", 10000, 10);

        int orderCount = 11;

        //when
        Assertions.assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);

        //then
    }

    @Test
    public void orderCancel() throws Exception {
        //given
        Member member = createMember();
        Item book = createItem("JPA BOOK", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.find(orderId);

        assertEquals(getOrder.getStatus(), OrderStatus.CANCEL, "주문취소시 상태는 CANCEL");
        assertEquals(10, book.getStockQuantity(), "주문취소시 물품 수량은 복구된다.");
    }

    private Item createItem(String name, int price, int quantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("userA");
        member.setAddress(new Address("Seoul", "GangNam", "777"));
        em.persist(member);
        return member;
    }

}