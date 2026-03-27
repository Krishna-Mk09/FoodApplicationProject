import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';

interface MenuItem {
    id: number;
    name: string;
    price: number;
    description: string;
    isVeg: boolean;
    image: string;
    rating: number;
    votes: number;
}

@Component({
    selector: 'app-restaurant-menu',
    standalone: true,
    imports: [CommonModule, Header, Footer],
    templateUrl: './restaurant-menu.html',
    styleUrl: './restaurant-menu.css'
})
export class RestaurantMenu implements OnInit {
    restaurantId: string | null = null;

    menuItems: MenuItem[] = [
        { id: 101, name: 'Chicken Biryani', price: 250, description: 'Richly flavored aromatic rice layered with marinated chicken pieces in a delicate blend of whole spices.', isVeg: false, image: 'https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?auto=format&fit=crop&q=80&w=200', rating: 4.5, votes: 1205 },
        { id: 102, name: 'Paneer Butter Masala', price: 220, description: 'Cottage cheese cubes cooked in a rich and creamy tomato based gravy.', isVeg: true, image: 'https://images.unsplash.com/photo-1551881192-002e02ab3d89?auto=format&fit=crop&q=80&w=200', rating: 4.4, votes: 856 },
        { id: 103, name: 'Classic Chicken Burger', price: 180, description: 'Crunchy chicken patty with fresh lettuce, tomatoes and our signature mayo.', isVeg: false, image: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&q=80&w=200', rating: 4.2, votes: 540 },
        { id: 104, name: 'Margherita Pizza', price: 299, description: 'Classic delight with 100% real mozzarella cheese.', isVeg: true, image: 'https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&q=80&w=200', rating: 4.6, votes: 2100 },
        { id: 105, name: 'Death By Chocolate', price: 210, description: 'A massive scoop of chocolate ice cream, chocolate brownie, hot chocolate fudge sauce, peanuts, and cherries.', isVeg: true, image: 'https://images.unsplash.com/photo-1557142046-c704a3adf8af?auto=format&fit=crop&q=80&w=200', rating: 4.8, votes: 3450 },
    ];

    constructor(private route: ActivatedRoute) { }

    ngOnInit() {
        this.restaurantId = this.route.snapshot.paramMap.get('id');
    }

    addToCart(item: MenuItem) {
        alert(`Added ${item.name} to cart!`);
    }
}
