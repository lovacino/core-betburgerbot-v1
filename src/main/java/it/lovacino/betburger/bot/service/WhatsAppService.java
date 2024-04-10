package it.lovacino.betburger.bot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    private static final Logger logger = LoggerFactory.getLogger("BOT");

    public boolean sendMessage(String whatsAppNumber, String message) {
        logger.info("[BOT] sendMessage START whatsAppNumber: {}, message: {}", whatsAppNumber, message);
        try {
            //TODO: IMPLEMENTARE LOGICA PER SPEDIZIONE MESSAGGIO TRAMITE API WHATSAPP
            logger.info("[BOT] sendMessage SENDED whatsAppNumber: {}, message: {}", whatsAppNumber, message);
            return true;
        } catch (Exception e) {
            logger.error("[BOT] sendMessage END", e);
        }
        logger.info("[BOT] sendMessage NOT SENDED whatsAppNumber: {}, message: {}", whatsAppNumber, message);
        return false;
    }
}
