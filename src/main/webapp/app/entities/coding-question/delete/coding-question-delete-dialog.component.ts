import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICodingQuestion } from '../coding-question.model';
import { CodingQuestionService } from '../service/coding-question.service';

@Component({
  templateUrl: './coding-question-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CodingQuestionDeleteDialogComponent {
  codingQuestion?: ICodingQuestion;

  protected codingQuestionService = inject(CodingQuestionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.codingQuestionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
