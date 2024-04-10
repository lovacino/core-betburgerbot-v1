import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BettingDetailComponent } from './betting-detail.component';

describe('Betting Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BettingDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BettingDetailComponent,
              resolve: { betting: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BettingDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load betting on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BettingDetailComponent);

      // THEN
      expect(instance.betting).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
