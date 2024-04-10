import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PeriodDetailComponent } from './period-detail.component';

describe('Period Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeriodDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PeriodDetailComponent,
              resolve: { period: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PeriodDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load period on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PeriodDetailComponent);

      // THEN
      expect(instance.period).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
