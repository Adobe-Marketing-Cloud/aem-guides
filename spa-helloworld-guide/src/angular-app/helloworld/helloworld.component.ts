import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-helloworld',
  host: { 'class': 'cmp-helloworld' },
  styleUrls:['./helloworld.component.css'],
  templateUrl: './helloworld.component.html',
})

export class HelloWorldComponent {
  @Input() displayMessage: string;
}
