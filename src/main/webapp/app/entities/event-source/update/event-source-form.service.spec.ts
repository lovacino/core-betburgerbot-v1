import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../event-source.test-samples';

import { EventSourceFormService } from './event-source-form.service';

describe('EventSource Form Service', () => {
  let service: EventSourceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EventSourceFormService);
  });

  describe('Service methods', () => {
    describe('createEventSourceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEventSourceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            home: expect.any(Object),
            away: expect.any(Object),
            league: expect.any(Object),
            eventName: expect.any(Object),
            bookmakerEventId: expect.any(Object),
            betTypeParam: expect.any(Object),
            koefLastModifiedAt: expect.any(Object),
            scannedAt: expect.any(Object),
            startedAt: expect.any(Object),
            updatedAt: expect.any(Object),
            betBurgerId: expect.any(Object),
            koef: expect.any(Object),
            bookmaker: expect.any(Object),
            sport: expect.any(Object),
            period: expect.any(Object),
            betType: expect.any(Object),
          }),
        );
      });

      it('passing IEventSource should create a new form with FormGroup', () => {
        const formGroup = service.createEventSourceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            home: expect.any(Object),
            away: expect.any(Object),
            league: expect.any(Object),
            eventName: expect.any(Object),
            bookmakerEventId: expect.any(Object),
            betTypeParam: expect.any(Object),
            koefLastModifiedAt: expect.any(Object),
            scannedAt: expect.any(Object),
            startedAt: expect.any(Object),
            updatedAt: expect.any(Object),
            betBurgerId: expect.any(Object),
            koef: expect.any(Object),
            bookmaker: expect.any(Object),
            sport: expect.any(Object),
            period: expect.any(Object),
            betType: expect.any(Object),
          }),
        );
      });
    });

    describe('getEventSource', () => {
      it('should return NewEventSource for default EventSource initial value', () => {
        const formGroup = service.createEventSourceFormGroup(sampleWithNewData);

        const eventSource = service.getEventSource(formGroup) as any;

        expect(eventSource).toMatchObject(sampleWithNewData);
      });

      it('should return NewEventSource for empty EventSource initial value', () => {
        const formGroup = service.createEventSourceFormGroup();

        const eventSource = service.getEventSource(formGroup) as any;

        expect(eventSource).toMatchObject({});
      });

      it('should return IEventSource', () => {
        const formGroup = service.createEventSourceFormGroup(sampleWithRequiredData);

        const eventSource = service.getEventSource(formGroup) as any;

        expect(eventSource).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEventSource should not enable id FormControl', () => {
        const formGroup = service.createEventSourceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEventSource should disable id FormControl', () => {
        const formGroup = service.createEventSourceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
