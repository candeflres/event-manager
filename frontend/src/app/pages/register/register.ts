import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Header } from '../../components/header/header';
import { Footer } from '../../components/footer/footer';
import { UserService } from '../../services/user-service';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [RouterModule, CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private service = inject(UserService);
  private fb = inject(FormBuilder)

  form = this.fb.nonNullable.group({
    firstName: [""],
    lastName: [""],
    phone: [""],
    email: [""],
    password: [""],
  })


  register(){
    if(this.form.invalid){
      return;
    }

    const newUser = this.form.getRawValue();

    if(newUser){
      this.service.register(newUser).subscribe(()=>{
        console.log("Usuario registrado");
      });
    }




  }

}
