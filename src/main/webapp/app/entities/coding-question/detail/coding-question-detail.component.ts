import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICodingQuestion } from '../coding-question.model';

@Component({
  selector: 'jhi-coding-question-detail',
  templateUrl: './coding-question-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CodingQuestionDetailComponent {
  codingQuestion = input<ICodingQuestion | null>(null);

  previousState(): void {
    window.history.back();
  }
}
