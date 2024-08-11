import { Component, Input } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';

@Component({
  selector: 'app-title-wrapper',
  standalone: true,
  imports: [DividerModule, ButtonModule],
  templateUrl: './title-wrapper.component.html',
  styleUrl: './title-wrapper.component.scss',
})
export class TitleWrapperComponent {
  @Input()
  title: string = '';
  @Input()
  description: string = '';
}
