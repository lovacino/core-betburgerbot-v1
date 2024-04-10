import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EventSourceDetailComponent } from './event-source-detail.component';

describe('EventSource Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventSourceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EventSourceDetailComponent,
              resolve: { eventSource: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EventSourceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load eventSource on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EventSourceDetailComponent);

      // THEN
      expect(instance.eventSource).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
