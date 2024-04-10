import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConfigParamDetailComponent } from './config-param-detail.component';

describe('ConfigParam Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigParamDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ConfigParamDetailComponent,
              resolve: { configParam: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConfigParamDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load configParam on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConfigParamDetailComponent);

      // THEN
      expect(instance.configParam).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
