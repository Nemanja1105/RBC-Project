import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { headerRoutes } from '../../../../config/header.config';
import { MobileNavbarComponent } from '../mobile-navbar/mobile-navbar.component';

@Component({
  selector: 'app-header-navbar',
  standalone: true,
  imports: [
    ToolbarModule,
    RouterLink,
    RouterLinkActive,
    ButtonModule,
    MobileNavbarComponent,
  ],
  templateUrl: './header-navbar.component.html',
  styleUrl: './header-navbar.component.scss',
})
export class HeaderNavbarComponent {
  routes = headerRoutes;
}
