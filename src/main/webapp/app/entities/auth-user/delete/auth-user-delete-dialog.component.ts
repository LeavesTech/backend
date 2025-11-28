import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAuthUser } from '../auth-user.model';
import { AuthUserService } from '../service/auth-user.service';

@Component({
  templateUrl: './auth-user-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AuthUserDeleteDialogComponent {
  authUser?: IAuthUser;

  protected authUserService = inject(AuthUserService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
