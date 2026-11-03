
package myproject7;


public class Myproject7 {

    public static void main(String[] args) {
    
        Customer customer1 = new Customer("Ahmet Yılmaz", "05551112233", "Gül Sok. No:5", "Melikgazi", "Kayseri");
        Customer customer2 = new Customer("Ayşe Kaya", "05442223344", "Atatürk Cad. No:12", "Kocasinan", "Kayseri");

        Order order1 = new Order(customer1);
        Order order2 = new Order(customer2);

        try {
            order1.addItem(new MenuItem("Burger", 120.0, 1));
            order1.addItem(new MenuItem("Burger", 120.0, 1));
            order1.addItem(new MenuItem("Patates", 40.0, 2));
            order1.addItem(new MenuItem("İçecek", 25.0, 1));

            System.out.println("Sipariş 1 Ara Toplam: " + order1.getSubtotal() + " TL");

            order1.updateQuantity("Patates", 1);
            order1.removeItem("İçecek");
        } catch (InvalidQuantityException | ItemNotFoundException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        Coupon validCoupon = new Coupon("INDIRIM20", 0.20, 150.0);
        Coupon invalidCoupon = new Coupon("INDIRIM50", 0.50, 500.0);

        try {
            order1.addItem(new MenuItem("Pizza", 150.0, -2));
        } catch (InvalidQuantityException e) {
            System.out.println("[YAKALANAN HATA 1] " + e.getMessage());
        }

        try {
            order1.removeItem("Tatlilar");
        } catch (ItemNotFoundException e) {
            System.out.println("[YAKALANAN HATA 2] " + e.getMessage());
        }

        try {
            order2.addItem(new MenuItem("Çay", 15.0, 2));
            order2.checkout();
        } catch (MinimumOrderException e) {
            System.out.println("[YAKALANAN HATA 3] " + e.getMessage());
        } catch (InvalidQuantityException e) {
            System.out.println("Hata: " + e.getMessage());
        }

        try {
            order1.applyCoupon(validCoupon);
            order1.checkout();

            order2.addItem(new MenuItem("Pide", 120.0, 1));
            order2.applyCoupon(invalidCoupon);
            order2.checkout();
        } catch (Exception e) {
            System.out.println("Checkout Hatası: " + e.getMessage());
        }

        System.out.println("----- SİSTEM İSTATİSTİKLERİ -----");
        System.out.println("Tamamlanan Sipariş Sayısı: " + Order.getCompletedOrders());
        System.out.println("Toplam Gelir: " + Order.getTotalRevenue() + " TL");
    }
}