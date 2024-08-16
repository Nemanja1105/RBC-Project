package org.unibl.etf.mybudgetbackend.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an operation is attempted on an account that has insufficient funds.
 * This exception indicates that the account balance is lower than the amount required for the transaction.
 */
public class InsufficientAccountBalanceException extends HttpException {
    public InsufficientAccountBalanceException() {
        super(HttpStatus.BAD_REQUEST, "Insufficient funds in the account.");
    }

    public InsufficientAccountBalanceException(Object data) {
        super(HttpStatus.BAD_REQUEST, data);
    }
}
