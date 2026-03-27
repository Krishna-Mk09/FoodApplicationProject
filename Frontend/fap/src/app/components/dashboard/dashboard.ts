import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';

interface Restaurant {
  id: number;
  name: string;
  cuisine: string;
  rating: number;
  eta: string;
  image: string;
  priceForTwo: string;
  promoted?: boolean;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, Header, Footer],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  restaurants: Restaurant[] = [
    { id: 1, name: 'Truffles', cuisine: 'Burger, American, Fast Food', rating: 4.5, eta: '30 min', image: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹600 for two', promoted: true },
    { id: 2, name: 'Meghana Foods', cuisine: 'Biryani, Andhra, South Indian', rating: 4.4, eta: '45 min', image: 'https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹800 for two' },
    { id: 3, name: 'Leon Grill', cuisine: 'Fast Food, Turkish, Wraps', rating: 4.2, eta: '25 min', image: 'https://images.unsplash.com/photo-1615719413546-198b25453f85?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹400 for two' },
    { id: 4, name: 'Empire Restaurant', cuisine: 'North Indian, Kebab, Biryani', rating: 4.1, eta: '40 min', image: 'https://images.unsplash.com/photo-1606491956689-2ea866880c84?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹500 for two' },
    { id: 5, name: 'Corner House Ice Cream', cuisine: 'Ice Cream, Desserts', rating: 4.8, eta: '20 min', image: 'https://images.unsplash.com/photo-1557142046-c704a3adf8af?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹250 for two' },
    { id: 6, name: 'KFC', cuisine: 'Burger, Fast Food, American', rating: 4.0, eta: '35 min', image: 'https://images.unsplash.com/photo-1513639776629-7b61b0ac49cb?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹400 for two' },
    { id: 7, name: 'Domino\'s Pizza', cuisine: 'Pizza, Italian, Fast Food', rating: 4.3, eta: '30 min', image: 'https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹400 for two' },
    { id: 8, name: 'A2B - Adyar Ananda Bhavan', cuisine: 'South Indian, Sweets, Street Food', rating: 4.2, eta: '35 min', image: 'https://images.unsplash.com/photo-1589301760014-d929f39ce9b1?auto=format&fit=crop&q=80&w=800', priceForTwo: '₹300 for two' },
  ];

  constructor(private router: Router) { }

  goToRestaurant(id: number) {
    this.router.navigate(['/restaurant', id]);
  }
}
