import {
  AbstractControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
} from '@angular/forms';

export function sufficientFundsValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const formGroup = control.parent as FormGroup;
    if (!formGroup) {
      return null;
    }

    const account = formGroup.get('account')?.value;
    const amount = control.value;
    const transactionType = formGroup.get('type')?.value;

    if (transactionType === 0 && account && amount > account.balance) {
      return { insufficientFunds: true };
    }
    return null;
  };
}
