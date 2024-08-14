import { DecimalPipe, NgClass, UpperCasePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { SkeletonModule } from 'primeng/skeleton';
import { TransactionDTO } from '../../../../models/transactions';

@Component({
  selector: 'app-transactions-data-view-items',
  standalone: true,
  imports: [NgClass, SkeletonModule, UpperCasePipe, DecimalPipe],
  templateUrl: './transactions-data-view-items.component.html',
  styleUrl: './transactions-data-view-items.component.scss',
})
export class TransactionsDataViewItemsComponent {
  @Input()
  transactions: TransactionDTO[] | undefined;
  @Input()
  isLoading: boolean = true;
  @Input()
  numOfRows: number = 5;
}
