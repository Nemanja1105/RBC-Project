import { Component, inject } from '@angular/core';
import { headerRoutes } from '../../../../config/header.config';
import { MenuModule } from 'primeng/menu';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { Router, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-mobile-navbar',
  standalone: true,
  imports: [MenuModule, ButtonModule, RouterLinkActive],
  templateUrl: './mobile-navbar.component.html',
  styleUrl: './mobile-navbar.component.scss',
})
export class MobileNavbarComponent {
  routes = headerRoutes;
  items: MenuItem[] | undefined;
  router = inject(Router);

  constructor() {
    this.items = [
      {
        label: 'Menu',
        items: headerRoutes.map((el) => {
          return {
            label: el.title,
            icon: el.icon,
            route: el.path,
            //command: () => this.router.navigate([el.path]),
          };
        }),
      },
    ];
  }
}
