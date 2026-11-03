
package myproject7;

import java.util.ArrayList;
import java.util.List;

public class Order {
   
    private static int nextOrderNumber = 1001;
    public static final double MIN_ORDER = 100.0;
    public static final double DELIVERY_FEE = 20.0;
    
    private static int completedOrders = 0;
    private static double totalRevenue = 0.0;

    private int orderNumber;
    private Customer customer;
    private List<MenuItem> items;
    private Coupon coupon;
    private boolean isCheckedOut;

    public Order(Customer customer) {
        this.orderNumber = nextOrderNumber++;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.coupon = null;
        this.isCheckedOut = false;
    }

   
    

    public void addItem(MenuItem item) throws InvalidQuantityException {
        for (MenuItem existingItem : items) {
            if (existingItem.getName().equalsIgnoreCase(item.getName())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(String name) throws ItemNotFoundException {
        boolean removed = items.removeIf(item -> item.getName().equalsIgnoreCase(name));
        if (!removed) {
            throw new ItemNotFoundException("Ürün bulunamadı: " + name);
        }
    }

    public void updateQuantity(String name, int quantity) throws ItemNotFoundException, InvalidQuantityException {
        for (MenuItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                item.setQuantity(quantity);
                return;
            }
        }
        throw new ItemNotFoundException("Güncellenecek ürün bulunamadı: " + name);
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (MenuItem item : items) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public void applyCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public double checkout() throws MinimumOrderException, IllegalStateException {
        if (isCheckedOut) {
            throw new IllegalStateException("Bu sipariş daha önce tamamlanmıştır!");
        }

        double subtotal = getSubtotal();
        if (subtotal < MIN_ORDER) {
            throw new MinimumOrderException("Minimum sipariş tutarı " + MIN_ORDER + " TL olmalıdır. Mevcut: " + subtotal + " TL");
        }

        double discount = 0.0;
        if (coupon != null && subtotal >= coupon.getMinSubtotal()) {
            discount = subtotal * coupon.getDiscountRate();
        }

        double total = subtotal - discount + DELIVERY_FEE;

        completedOrders++;
        totalRevenue += total;
        this.isCheckedOut = true;

        System.out.println("\n========== FİŞ ==========");
        System.out.println("Sipariş No: " + orderNumber);
        System.out.println("Müşteri: " + customer.getName());
        System.out.println("Adres: " + customer.getAddress());
        System.out.println("--- Ürünler ---");
        for (MenuItem item : items) {
            System.out.println("- " + item);
        }
        System.out.println("Ara Toplam: " + subtotal + " TL");
        System.out.println("Kupon İndirimi: -" + discount + " TL");
        System.out.println("Teslimat Ücreti: " + DELIVERY_FEE + " TL");
        System.out.println("TOPLAM: " + total + " TL");
        System.out.println("==========================\n");

        return total;
    }

    public static int getCompletedOrders() {
        return completedOrders;
    }

    public static double getTotalRevenue() {
        return totalRevenue;
    }
}

