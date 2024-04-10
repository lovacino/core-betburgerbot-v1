import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BookmakerDetailComponent } from './bookmaker-detail.component';

describe('Bookmaker Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookmakerDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BookmakerDetailComponent,
              resolve: { bookmaker: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BookmakerDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load bookmaker on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BookmakerDetailComponent);

      // THEN
      expect(instance.bookmaker).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
