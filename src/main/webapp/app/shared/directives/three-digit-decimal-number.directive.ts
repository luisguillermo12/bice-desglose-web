import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
    selector: '[jhiThreeDigitDecimalNumber]'
})
export class ThreeDigitDecimalNumberDirective {
    private COMMA_SYMBOL = ',';
    private DOT_SYMBOL = '.';
    private regex: RegExp = new RegExp(/^(\d{1,3})([\.|,](\d{1,3}))?$/g);
    private specialKeys: Array<string> = [ 'Backspace', 'Delete', 'Tab', 'Escape', 'Enter', 'Home', 'End', 'ArrowLeft', 'ArrowRight', 'Clear', 'Copy' ];

    @HostListener('keydown', [ '$event' ])
    onKeyDown(event: KeyboardEvent) {
        // Allow Backspace, tab, end, and home keys
        if (this.specialKeys.indexOf(event.key) !== -1) {
            return;
        }
        const input = event.target as HTMLInputElement;
        const current: string = input.value;
        const position = input.selectionStart;
        const next: string = [ current.slice(0, position), event.key, current.slice(position) ].join('');
        if (next && event.key && event.key !== this.COMMA_SYMBOL && event.key !== this.DOT_SYMBOL) {
            if (!String(next).match(this.regex)) {
                event.preventDefault();
            }
        } else if (next && event.key && (event.key === this.COMMA_SYMBOL || event.key === this.DOT_SYMBOL)) {
            if ((current.includes(this.COMMA_SYMBOL) && event.key === this.COMMA_SYMBOL) || (current.includes(this.COMMA_SYMBOL) && event.key === this.DOT_SYMBOL)) {
                event.preventDefault();
            }
        }
    }
}
