import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function positiveNumberValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (control.value === null || control.value === undefined) {
      return null;
    }
    const value = parseFloat(control.value);
    if (isNaN(value) || value <= 0) {
      return { positiveNumber: true };
    }
    return null;
  };
}
