import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BetTypeDetailComponent } from './bet-type-detail.component';

describe('BetType Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BetTypeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BetTypeDetailComponent,
              resolve: { betType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BetTypeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load betType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BetTypeDetailComponent);

      // THEN
      expect(instance.betType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
