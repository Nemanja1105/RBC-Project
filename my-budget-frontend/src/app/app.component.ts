import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderNavbarComponent } from './shared/components/header/header-navbar/header-navbar.component';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderNavbarComponent, ToastModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {}
