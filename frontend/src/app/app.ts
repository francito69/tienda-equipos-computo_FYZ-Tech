import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CartIconComponent } from './components/cart-icon/cart-icon.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CartIconComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'fyztech';
}
