import {
  AbstractControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';

// export function sufficientFundsValidator(): ValidatorFn {
//   return (control: AbstractControl): ValidationErrors | null => {
//     const formGroup = control.parent as FormGroup;
//     if (!formGroup) {
//       return null;
//     }

//     const account = formGroup.get('account')?.value;
//     const amount = control.value;
//     const transactionType = formGroup.get('type')?.value;

//     if (transactionType === 0 && account && amount > account.balance) {
//       return { insufficientFunds: true };
//     }
//     return null;
//   };
// }
export function sufficientFundsValidator(): ValidatorFn {
  return (formGroup: AbstractControl): ValidationErrors | null => {
    const accountControl = formGroup.get('account');
    const amountControl = formGroup.get('amount');
    const transactionTypeControl = formGroup.get('type');

    if (!accountControl || !amountControl || !transactionTypeControl) {
      return null;
    }

    const account = accountControl.value;
    const amount = amountControl.value;
    const transactionType = transactionTypeControl.value;

    if (transactionType === 0 && account && amount > account.balance) {
      return { insufficientFunds: true };
    }

    return null;
  };
}
