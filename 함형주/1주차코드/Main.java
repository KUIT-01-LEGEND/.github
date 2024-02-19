package ;

import lombok.Data;
import lombok.NoArgsConstructor;

public class Main {

    public static void main(String[] args) {
        Discount discount = new Discount();
        double 처음가격 = 15000;

        // 고정 할인
        double 고정할인가격 = 3000;
        Price price1 = discount.discount(
                처음가격,
                () -> 처음가격 - 고정할인가격);
        System.out.println("price1 = " + price1); // 15000 - 3000 = 12000

        // 퍼센트 할인
        double 퍼센트할인 = 0.3; // %
        Price price2 = discount.discount(
                처음가격,
                () -> 처음가격 - (처음가격 * 퍼센트할인));
        System.out.println("price2 = " + price2); // 15000 * 0.7 = 10500

        // 쿠폰 선할인 (퍼센트할인)
        Coupon coupon = new Coupon();
        Price price3 = discount.discount(
                처음가격,
                () -> {
                    double 중간가격 = coupon.할인(처음가격);
                    return 중간가격 - (중간가격 * 퍼센트할인);
                });
        System.out.println("price3 = " + price3); // 15000 - 3000 = 12000, 12000 * 0.7 = 8400

        // 쿠폰 이후 할인 (퍼센트할인)
        Price price4 = discount.discount(
                처음가격,
                () -> {
                    double 중간가격 = 처음가격 - (처음가격 * 퍼센트할인);
                    return coupon.할인(중간가격);
                }
        );
        System.out.println("price4 = " + price4); // 15000 * 0.7 = 10500, 10500 - 3000 = 7500


    }

    @Data
    static class Price {

        public Price(double 처음가격) {
            this.처음가격 = 처음가격;
            this.최종가격 = 처음가격;
        }

        public void 할인(double 최종가격) {
            this.최종가격 = 최종가격;
        }

        double 처음가격;
        double 최종가격;

        public String toString() {
            return "처음가격 = " + 처음가격 + ", 최종가격 = " + 최종가격;
        }
    }

    @NoArgsConstructor
    static class Coupon {
        private double 고정할인 = 3000;

        public double 할인(double input) {
            return input - 고정할인;
        }

    }

    static class Discount {

        public Price discount(double input, DiscountPolicy discountPolicy) {
            Price price = new Price(input);
            double 최종가격 = discountPolicy.policy();
            price.할인(최종가격);
            return price;
        }
    }

    interface DiscountPolicy {
        double policy();
    }
}
