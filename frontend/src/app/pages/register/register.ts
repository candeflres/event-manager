import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Header } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-register',
  imports: [RouterModule, CommonModule, Header, Footer],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {}
