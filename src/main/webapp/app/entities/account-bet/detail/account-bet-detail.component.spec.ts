import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccountBetDetailComponent } from './account-bet-detail.component';

describe('AccountBet Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountBetDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccountBetDetailComponent,
              resolve: { accountBet: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccountBetDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load accountBet on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccountBetDetailComponent);

      // THEN
      expect(instance.accountBet).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
