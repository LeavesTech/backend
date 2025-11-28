import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAuthUser } from '../auth-user.model';

@Component({
  selector: 'jhi-auth-user-detail',
  templateUrl: './auth-user-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AuthUserDetailComponent {
  authUser = input<IAuthUser | null>(null);

  previousState(): void {
    window.history.back();
  }
}
